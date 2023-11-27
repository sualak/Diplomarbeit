package at.dertanzbogen.api.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

// Builder pattern for TestRestTemplate
public class ExchangeBuilder
{
    private final HttpMethod method;
    private final String url;
    private final HttpHeaders headers;

    private ExchangeBuilder(HttpMethod method, String url)
    {
        this.method = method;
        this.url = url;
        this.headers = new HttpHeaders();
    }

    public static ExchangeBuilder of(HttpMethod method, String url)
    {
        return new ExchangeBuilder(method, url);
    }

    public ExchangeBuilder withHeader(String headerName, String headerValue)
    {
        this.headers.set(headerName, headerValue);
        return this;
    }

    public ExchangeBuilder withBasicAuth(String username, String password)
    {
        this.headers.setBasicAuth(username, password);
        return this;
    }

    public ResponseEntity<String> exchange(TestRestTemplate restTemplate)
    {
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, method, httpEntity, String.class);
    }

    public <T> ResponseEntity<T> exchange(TestRestTemplate restTemplate, Class<T> responseType)
    {
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, method, httpEntity, responseType);
    }
}