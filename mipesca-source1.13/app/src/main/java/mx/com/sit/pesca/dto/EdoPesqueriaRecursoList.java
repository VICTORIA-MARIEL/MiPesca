package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.EdoPesqueriaRecursoEntity;
import mx.com.sit.pesca.entity.RecursoEntity;

public class EdoPesqueriaRecursoList {
    @SerializedName("comboPesqueriaRecursos")
    private List<EdoPesqueriaRecursoEntity> pesqueriaRecursos;

    public EdoPesqueriaRecursoList(){

    }

    public List<EdoPesqueriaRecursoEntity> getPesqueriaRecursos() {
        return pesqueriaRecursos;
    }

    public void setPesqueriaRecursos(List<EdoPesqueriaRecursoEntity> pesqueriaRecursos) {
        this.pesqueriaRecursos = pesqueriaRecursos;
    }
}
