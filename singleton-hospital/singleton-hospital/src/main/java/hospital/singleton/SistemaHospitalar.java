package hospital.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SistemaHospitalar {


    private static volatile SistemaHospitalar instancia;

    private String nomeHospital;
    private String cnpj;
    private int totalLeitos;
    private int leitosOcupados;
    private String medicoPlantao;
    private boolean sistemaEmOperacao;
    private final List<String> logEventos;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private SistemaHospitalar() {
        this.nomeHospital      = "Hospital Geral";
        this.cnpj              = "00.000.000/0000-00";
        this.totalLeitos       = 100;
        this.leitosOcupados    = 0;
        this.sistemaEmOperacao = false;
        this.logEventos        = new ArrayList<>();
        registrarEvento("Sistema hospitalar iniciado.");
    }

    public static SistemaHospitalar getInstance() {
        if (instancia == null) {
            synchronized (SistemaHospitalar.class) {
                if (instancia == null) {
                    instancia = new SistemaHospitalar();
                }
            }
        }
        return instancia;
    }


    public void iniciarPlantao(String medico) {
        this.medicoPlantao      = medico;
        this.sistemaEmOperacao  = true;
        registrarEvento("Plantão iniciado pelo Dr(a). " + medico + ".");
    }

    public void encerrarPlantao() {
        registrarEvento("Plantão encerrado pelo Dr(a). " + medicoPlantao + ".");
        this.medicoPlantao      = null;
        this.sistemaEmOperacao  = false;
    }


    public void internarPaciente(String nomePaciente) {
        if (!sistemaEmOperacao) {
            throw new IllegalStateException("Nenhum médico em plantão. Operação não permitida.");
        }
        if (leitosOcupados >= totalLeitos) {
            throw new IllegalStateException("Não há leitos disponíveis no momento.");
        }
        leitosOcupados++;
        registrarEvento(String.format(
            "Paciente '%s' internado por Dr(a). %s. Leitos: %d/%d.",
            nomePaciente, medicoPlantao, leitosOcupados, totalLeitos));
    }

    public void darAlta(String nomePaciente) {
        if (!sistemaEmOperacao) {
            throw new IllegalStateException("Nenhum médico em plantão. Operação não permitida.");
        }
        if (leitosOcupados <= 0) {
            throw new IllegalStateException("Nenhum paciente internado para receber alta.");
        }
        leitosOcupados--;
        registrarEvento(String.format(
            "Alta concedida ao paciente '%s' por Dr(a). %s. Leitos: %d/%d.",
            nomePaciente, medicoPlantao, leitosOcupados, totalLeitos));
    }


    public void registrarEvento(String evento) {
        String entrada = "[" + LocalDateTime.now().format(FORMATTER) + "] " + evento;
        logEventos.add(entrada);
    }

    public List<String> getLogEventos() {
        return Collections.unmodifiableList(logEventos);
    }


    public String getNomeHospital()  { return nomeHospital; }
    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
        registrarEvento("Nome do hospital alterado para: " + nomeHospital);
    }

    public String getCnpj()          { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public int getTotalLeitos()      { return totalLeitos; }
    public void setTotalLeitos(int totalLeitos) {
        this.totalLeitos = totalLeitos;
        registrarEvento("Total de leitos atualizado para: " + totalLeitos);
    }

    public int getLeitosOcupados()   { return leitosOcupados; }
    public int getLeitosDisponiveis(){ return totalLeitos - leitosOcupados; }

    public String getMedicoPlantao() { return medicoPlantao; }
    public boolean isSistemaEmOperacao() { return sistemaEmOperacao; }
}
