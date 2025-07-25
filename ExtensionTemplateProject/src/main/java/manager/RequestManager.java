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
    private boolean ignoreScope = false;

    public RequestManager(MontoyaApi montoyaApi, FlowManager flowManager) {
        this.flowManager = flowManager;
        this.montoyaApi = montoyaApi;
    }

    public void handleIncomingRequest(InterceptedRequest interceptedRequest) {
        //if (!ignoreScope && !interceptedRequest.isInScope()) {
        //     return;
        // }
        // ADD CONFIGURATION FOR IF IGNORESCOPE IS FALSE
        if (!flowManager.isFlowActive() || !interceptedRequest.isInScope()) {
            return;
        }
        
        interceptedRequest.annotations().setHighlightColor(HighlightColor.BLUE);
        flowManager.addRequestToActiveFlow(interceptedRequest);
    }

    public void handleIncomingResponse(InterceptedResponse interceptedResponse) {
        if (!flowManager.isFlowActive()) {
            return;
        }

        flowManager.addResponseToFlow(interceptedResponse);
    }
}