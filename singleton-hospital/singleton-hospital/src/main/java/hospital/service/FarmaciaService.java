package hospital.service;

import hospital.singleton.SistemaHospitalar;

public class FarmaciaService {

    public void dispensarMedicamento(String paciente, String medicamento, String dosagem) {
        SistemaHospitalar sistema = SistemaHospitalar.getInstance();

        if (!sistema.isSistemaEmOperacao()) {
            throw new IllegalStateException("Sem médico em plantão. Farmácia suspensa.");
        }

        String msg = String.format(
            "Medicamento '%s' (%s) dispensado para '%s'. Autorizado por Dr(a). %s.",
            medicamento, dosagem, paciente, sistema.getMedicoPlantao());

        sistema.registrarEvento(msg);
        System.out.println("[FarmaciaService] " + msg);
    }
}
