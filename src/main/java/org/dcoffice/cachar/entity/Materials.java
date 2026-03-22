package org.dcoffice.cachar.entity;

import java.util.Arrays;
import java.util.List;

public class Materials {

    private boolean isSubmitted;
    private Long submittedAt;
    private List<MaterialItem> items;

    public Materials() {}

    public static Materials defaultMaterials() {
        Materials m = new Materials();
        m.setSubmitted(false);
        m.setSubmittedAt(null);
        m.setItems(Arrays.asList(
            new MaterialItem("EVM Machine", false),
            new MaterialItem("VVPAT", false),
            new MaterialItem("Ballot Unit", false),
            new MaterialItem("Control Unit", false),
            new MaterialItem("Pen", false),
            new MaterialItem("Sealing Material", false),
            new MaterialItem("Statutory Forms", false),
            new MaterialItem("Indelible Ink", false)
        ));
        return m;
    }

    public boolean isSubmitted() { return isSubmitted; }
    public void setSubmitted(boolean submitted) { isSubmitted = submitted; }

    public Long getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Long submittedAt) { this.submittedAt = submittedAt; }

    public List<MaterialItem> getItems() { return items; }
    public void setItems(List<MaterialItem> items) { this.items = items; }
}
