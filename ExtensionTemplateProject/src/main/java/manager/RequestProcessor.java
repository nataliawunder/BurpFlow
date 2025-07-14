package manager;

import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class RequestProcessor implements ProxyRequestHandler {

    private final RequestManager requestManager;

    public RequestProcessor(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        // TODO 
        requestManager.handleIncomingRequest(interceptedRequest);
        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        // TODO
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}