package manager;

import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.http.handler.TimingData;
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
        if (interceptedResponse.isPresent()) {
            return String.valueOf(interceptedResponse.get().statusCode());
        }
        if (httpRequestResponse != null && httpRequestResponse.response() != null) {
            return String.valueOf(httpRequestResponse.response().statusCode());
        }
        return "";
    }

    public String mimeType() {
        if (interceptedResponse.isPresent()) {
            return interceptedResponse.get().mimeType().toString();
        }
        if (httpRequestResponse != null && httpRequestResponse.response() != null) {
            return httpRequestResponse.response().mimeType().toString();
        }
        return "";
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
        if (interceptedRequest != null) {
            return null;
        }

        if (httpRequestResponse != null) {
            Optional<TimingData> timing = httpRequestResponse.timingData();
            if (timing.isPresent()) {
                return timing.get().timeRequestSent();
            }
        }

        return null;
    }
}
