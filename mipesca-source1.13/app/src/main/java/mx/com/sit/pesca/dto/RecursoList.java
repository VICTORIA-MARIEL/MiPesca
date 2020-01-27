package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.RecursoEntity;

public class RecursoList {
    @SerializedName("comboRecursos")
    private List<RecursoEntity> recursos;

    public RecursoList(){

    }

    public List<RecursoEntity> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<RecursoEntity> recursos) {
        this.recursos = recursos;
    }
}
