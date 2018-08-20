package io.kong.incheon.nfc_check.item;

import android.view.View;

public class ListViewBtnItem {

    private String txtTItle ;
    private String txtDate;
    private String txtProfessor;
    private String txtIndex;
    public View.OnClickListener onClickListener;

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

    public String getTxtIndex() {
        return txtIndex;
    }

    public void setTxtIndex(String txtIndex) {
        this.txtIndex = txtIndex;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
