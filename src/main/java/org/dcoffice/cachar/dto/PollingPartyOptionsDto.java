package org.dcoffice.cachar.dto;

import java.util.List;

public class PollingPartyOptionsDto {

    private List<String> pollingStations;
    private List<String> partyNames;

    public PollingPartyOptionsDto() {
    }

    public PollingPartyOptionsDto(List<String> pollingStations, List<String> partyNames) {
        this.pollingStations = pollingStations;
        this.partyNames = partyNames;
    }

    public List<String> getPollingStations() {
        return pollingStations;
    }

    public void setPollingStations(List<String> pollingStations) {
        this.pollingStations = pollingStations;
    }

    public List<String> getPartyNames() {
        return partyNames;
    }

    public void setPartyNames(List<String> partyNames) {
        this.partyNames = partyNames;
    }
}
