package config;

public class Config {
    private boolean usingProxyNumbers = true;

    public boolean isUsingProxyNumbers() {
        return usingProxyNumbers;
    }

    public void setUsingProxyNumbers(boolean useProxyNumbers) {
        this.usingProxyNumbers = useProxyNumbers;
    }
}