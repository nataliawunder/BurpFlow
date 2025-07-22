package manager;

import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import java.util.ArrayList;
import java.util.List;

public class Flow {
    private final String flowName;
    private final List<ProxyHttpRequestResponse> requests;
    private boolean isActive;

    public Flow(String flowName) {
        this.flowName = flowName;
        this.requests = new ArrayList<>();
        this.isActive = false;
    }

    public void addRequest(ProxyHttpRequestResponse request) {
        requests.add(request);
        System.out.println(flowName + " now has " + requests.size() + " entries");
    }

    // public void addRequests(List<HttpRequest> requests) {
    //     this.requests.addAll(requests);
    // }

    public void removeRequest(ProxyHttpRequestResponse request) {
        requests.remove(request);
    }

    // public void removeRequests(List<HttpRequest> requests) {
    //     this.requests.removeAll(requests);
    // }

    public List<ProxyHttpRequestResponse> getRequests() {
        return requests;
    }

    public String getFlowName() {
        return flowName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
    
