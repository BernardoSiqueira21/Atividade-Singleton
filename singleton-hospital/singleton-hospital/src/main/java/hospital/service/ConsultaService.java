package hospital.service;

import hospital.singleton.SistemaHospitalar;


public class ConsultaService {

    public void agendarConsulta(String paciente, String especialidade) {
        SistemaHospitalar sistema = SistemaHospitalar.getInstance();

        if (!sistema.isSistemaEmOperacao()) {
            throw new IllegalStateException("Sem médico em plantão. Consultas suspensas.");
        }

        String msg = String.format(
            "Consulta de '%s' agendada em %s com Dr(a). %s.",
            paciente, especialidade, sistema.getMedicoPlantao());

        sistema.registrarEvento(msg);
        System.out.println("[ConsultaService] " + msg);
    }

    public void realizarConsulta(String paciente, String diagnostico) {
        SistemaHospitalar sistema = SistemaHospitalar.getInstance();

        if (!sistema.isSistemaEmOperacao()) {
            throw new IllegalStateException("Sem médico em plantão. Consultas suspensas.");
        }

        String msg = String.format(
            "Consulta do paciente '%s' realizada. Diagnóstico: %s. Médico: Dr(a). %s.",
            paciente, diagnostico, sistema.getMedicoPlantao());

        sistema.registrarEvento(msg);
        System.out.println("[ConsultaService] " + msg);
    }
}
