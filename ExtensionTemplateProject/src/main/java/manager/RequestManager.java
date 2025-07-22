package manager;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.proxy.Proxy;
import burp.api.montoya.scope.Scope;
import burp.api.montoya.core.HighlightColor;

public class RequestManager {

    private final FlowManager flowManager;
    private final MontoyaApi montoyaApi;

    public RequestManager(MontoyaApi montoyaApi, FlowManager flowManager) {
        this.flowManager = flowManager;
        this.montoyaApi = montoyaApi;
    }

    // should it be intercepted? look at https://portswigger.github.io/burp-extensions-montoya-api/javadoc/burp/api/montoya/proxy/Proxy.html#registerResponseHandler(burp.api.montoya.proxy.http.ProxyResponseHandler)
    public void handleIncomingRequest(InterceptedRequest interceptedRequest) {
        // TODO
        // NEED TO CHECK IF SCOPE IS SET, write that it is assumed a scope is set, 
        // || !interceptedRequest.isInScope()
        if (!flowManager.isFlowActive() || !interceptedRequest.isInScope()) {
            return;
        }
        montoyaApi.logging().logToOutput("RequestManager request " + interceptedRequest.messageId());
        interceptedRequest.annotations().setHighlightColor(HighlightColor.BLUE);
        flowManager.addRequestToActiveFlow(interceptedRequest);
    }

    public void handleIncomingResponse(InterceptedResponse interceptedResponse) {
        if (!flowManager.isFlowActive()) {
            return;
        }
        montoyaApi.logging().logToOutput("RequestManager response " + interceptedResponse.messageId());
        flowManager.addResponseToFlow(interceptedResponse);
    }
}
