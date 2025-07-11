package manager;

import burp.api.montoya.http.message.requests.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class Flow {
    private final String flowName;
    private final List<HttpRequest> requests;
    private boolean isActive;

    public Flow(String flowName) {
        this.flowName = flowName;
        this.requests = new ArrayList<>();
        this.isActive = false;
    }

    public void addRequest(HttpRequest request) {
        requests.add(request);
    }

    // public void addRequests(List<HttpRequest> requests) {
    //     this.requests.addAll(requests);
    // }

    public void removeRequest(HttpRequest request) {
        requests.remove(request);
    }

    // public void removeRequests(List<HttpRequest> requests) {
    //     this.requests.removeAll(requests);
    // }

    public List<HttpRequest> getRequests() {
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
    
