package io.kong.incheon.nfc_check.item;

public class ListViewBtnItem {

    private String txtTItle ;
    private String txtDate;
    private String txtProfessor;

    public void setTextTitle(String txtTItle) {
        this.txtTItle = txtTItle ;
    }

    public String getTextTitle() {
        return this.txtTItle ;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }

    public String getTxtProfessor() {
        return txtProfessor;
    }

    public void setTxtProfessor(String txtProfessor) {
        this.txtProfessor = txtProfessor;
    }

}
