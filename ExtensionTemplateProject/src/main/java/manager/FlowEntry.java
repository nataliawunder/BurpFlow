package manager;

import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import java.util.Optional;

public class FlowEntry {
    private final InterceptedRequest request;
    private Optional<InterceptedResponse> response = Optional.empty();

    public FlowEntry(InterceptedRequest req) {
        this.request = req;
    }

    public InterceptedRequest getRequest() {
        return request;
    }

    public Optional<InterceptedResponse> getResponse() {
        return response;
    }

    public void setResponse(InterceptedResponse response) {
        this.response = Optional.of(response);
    }

    public long messageId() {
        return request.messageId();
    }
}
