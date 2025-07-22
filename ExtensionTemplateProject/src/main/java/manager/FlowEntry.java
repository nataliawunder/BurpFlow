package manager;

import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.http.message.HttpRequestResponse;

import java.time.ZonedDateTime;
import java.util.Optional;

public class FlowEntry {
    private final InterceptedRequest interceptedRequest;
    private Optional<InterceptedResponse> interceptedResponse = Optional.empty();
    private final HttpRequestResponse httpRequestResponse;

    // context menu adds
    public FlowEntry(HttpRequestResponse httpRequestResponse) {
        this.httpRequestResponse = httpRequestResponse;
        this.interceptedRequest = null;   
    }

    public FlowEntry(InterceptedRequest interceptedRequest) {
        this.interceptedRequest = interceptedRequest;
        this.httpRequestResponse = null;
    }

    public InterceptedRequest getRequest() {
        return interceptedRequest;
    }

    public Optional<InterceptedResponse> getResponse() {
        return interceptedResponse;
    }

    public void setResponse(InterceptedResponse response) {
        this.interceptedResponse = Optional.of(response);
    }

    public String messageId() {
        if (interceptedRequest != null) {
            return String.valueOf(interceptedRequest.messageId());
        }
        return "";
    }

    public String host() {
        if (interceptedRequest != null) {
            return interceptedRequest.httpService().host();
        }
        return httpRequestResponse.httpService().host();
    }

    public String method() {
        if (interceptedRequest != null) {
            return interceptedRequest.method();
        }
        return httpRequestResponse.request().method();
    }

    public String url() {
        if (interceptedRequest != null) {
            return interceptedRequest.url();
        }
        return httpRequestResponse.request().url();
    }

    public String status() {
        if (interceptedResponse != null) {
            return interceptedResponse.statusCode();
        }
        return String.valueOf(httpRequestResponse.response().statusCode());
    }

    public String mimeType() {
        if (interceptedResponse != null) {
            return interceptedResponse.mimeType();
        }
        return httpRequestResponse.response().mimeType().toString();
    }

    public String notes() {
        if (interceptedRequest != null) {
            return interceptedRequest.annotations().notes();
        }
        return httpRequestResponse.annotations().notes();
    }

    public String ip() {
        if (interceptedRequest != null) {
            return interceptedRequest.httpService().ipAddress();
        }
        return httpRequestResponse.httpService().ipAddress();
    }

    public ZonedDateTime time() {
        if (interceptedResponse != null) {
            return interceptedResponse.time();
        }
        return httpRequestResponse.timingData().ZonedDateTime();
    }
}
