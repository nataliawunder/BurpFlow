package manager;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.HighlightColor;

public class RequestManager {

    private final FlowManager flowManager;
    //private final MontoyaApi montoyaApi;

    public RequestManager(FlowManager flowManager) {
        this.flowManager = flowManager;
        // this.montoyaApi = montoyaApi;
    }

    // should it be intercepted? look at https://portswigger.github.io/burp-extensions-montoya-api/javadoc/burp/api/montoya/proxy/Proxy.html#registerResponseHandler(burp.api.montoya.proxy.http.ProxyResponseHandler)
    public void handleIncomingRequest(InterceptedRequest interceptedRequest) {
        // TODO
        if (!flowManager.isFlowActive()) {
            return;
        }
        // montoyaApi.logging().logToOutput(interceptedRequest.messageId());
        interceptedRequest.annotations().setHighlightColor(HighlightColor.BLUE);
        flowManager.addRequestToActiveFlow((HttpRequestResponse) interceptedRequest);
    }

    public void handleIncomingResponse(InterceptedResponse interceptedResponse) {
        // TODO
    }
}
