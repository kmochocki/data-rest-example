package com.kmochocki.datarestexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceUpdateTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChildResourceRepository childResourceRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String MAIN_RESOURCE_JSON_BODY = """
            {
              "description": "main res",
              "childResources": [
                %s
              ]
            }
            """;

    @Test
    void shouldCreateMainResourceWithSingleChildResource() throws JsonProcessingException {
        // given
        var first = createChildResource("first");

        // when
        var response = postMainResourceWithOneChild(first);

        // then
        var jsonNode = objectMapper.readTree(response.getBody());
        var childrenResponse = restTemplate.getForObject(jsonNode.get("_links").get("childResources").get("href").asText(), String.class);
        assertThat(childrenResponse).contains("first").doesNotContain("second");
    }

    @Test
    void shouldCreateMainResourceWithTwoChildResources() throws JsonProcessingException {
        // given
        var first = createChildResource("first");
        var second = createChildResource("second");

        // when
        var response = postMainResourceWithTwoChildren(first, second);

        // then
        var jsonNode = objectMapper.readTree(response.getBody());
        var childrenResponse = restTemplate.getForObject(jsonNode.get("_links").get("childResources").get("href").asText(), String.class);
        assertThat(childrenResponse).contains("first", "second");
    }

    @Test
    void shouldAddChildResourceUsingPatchMethod() throws JsonProcessingException {
        performAddingChildUsing(HttpMethod.PATCH);
    }

    @Test
    void shouldAddChildResourceUsingPutMethod() throws JsonProcessingException {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // allows using PATCH

        performAddingChildUsing(HttpMethod.PUT);
    }

    private void performAddingChildUsing(HttpMethod httpMethod) throws JsonProcessingException {
        // given
        var first = createChildResource("first");
        var mainResourceResponse = postMainResourceWithOneChild(first);
        var mainResourceLink = objectMapper.readTree(mainResourceResponse.getBody()).get("_links").get("self").get("href").asText();

        // when
        var second = createChildResource("second");
        var twoChildRequestBody = MAIN_RESOURCE_JSON_BODY.formatted("\"/" + first.getId() + "\", \"/" + second.getId() + "\"");
        var twoChildResponse = restTemplate.exchange(
                mainResourceLink,
                httpMethod,
                toHttpEntity(twoChildRequestBody),
                String.class
        );

        // then
        var twoChildJsonNode = objectMapper.readTree(twoChildResponse.getBody());
        var twoChildChildrenResponse = restTemplate.getForObject(twoChildJsonNode.get("_links").get("childResources").get("href").asText(), String.class);
        assertThat(twoChildChildrenResponse).contains("first", "second");
    }

    @Test
    void shouldRemoveChildResourceUsingPatchMethod() throws JsonProcessingException {
        performRemovingChildUsing(HttpMethod.PATCH);
    }

    @Test
    void shouldRemoveChildResourceUsingPutMethod() throws JsonProcessingException {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // allows using PATCH

        performRemovingChildUsing(HttpMethod.PUT);
    }

    private void performRemovingChildUsing(HttpMethod httpMethod) throws JsonProcessingException {
        // given
        var first = createChildResource("first");
        var second = createChildResource("second");
        var mainResourceResponse = postMainResourceWithTwoChildren(first, second);
        var mainResourceLink = objectMapper.readTree(mainResourceResponse.getBody()).get("_links").get("self").get("href").asText();

        // when
        var oneChildRequestBody = MAIN_RESOURCE_JSON_BODY.formatted("\"/" + first.getId() + "\"");
        var oneChildResponse = restTemplate.exchange(
                mainResourceLink,
                httpMethod,
                toHttpEntity(oneChildRequestBody),
                String.class
        );

        // then
        var oneChildJsonNode = objectMapper.readTree(oneChildResponse.getBody());
        var oneChildChildrenResponse = restTemplate.getForObject(oneChildJsonNode.get("_links").get("childResources").get("href").asText(), String.class);
        assertThat(oneChildChildrenResponse).contains("first").doesNotContain("second");
    }

    private ResponseEntity<String> postMainResourceWithOneChild(ChildResource first) {
        var requestBody = MAIN_RESOURCE_JSON_BODY.formatted("\"/" + first.getId() + "\"");
        return postMainResource(requestBody);
    }

    private ResponseEntity<String> postMainResourceWithTwoChildren(ChildResource first, ChildResource second) {
        var requestBody = MAIN_RESOURCE_JSON_BODY.formatted("\"/" + first.getId() + "\", \"/" + second.getId() + "\"");
        return postMainResource(requestBody);
    }

    private ResponseEntity<String> postMainResource(String requestBody) {
        return restTemplate.exchange(
                "/mainResources",
                HttpMethod.POST,
                toHttpEntity(requestBody),
                String.class
        );
    }

    private static HttpEntity<String> toHttpEntity(String jsonBody) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(jsonBody, headers);
    }

    private ChildResource createChildResource(String name) {
        var resource = new ChildResource(name);
        childResourceRepository.save(resource);
        return resource;
    }
}
