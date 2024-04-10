package de.adesso.communication.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("software-pico.mqtt")
public class MqttProperties {

    private String brokerIPAddress = "0.0.0.0";
    private int brokerPort = 1883;
    private String userName = "testUser";
    private String password = "testPwd";

    public String getBrokerIPAddress() {
        return brokerIPAddress;
    }
    public void setBrokerIPAddress(String brokerIPAddress) {
        this.brokerIPAddress = brokerIPAddress;
    }
    public int getBrokerPort() {
        return brokerPort;
    }
    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
