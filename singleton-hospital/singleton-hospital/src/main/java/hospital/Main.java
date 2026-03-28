package hospital;

import hospital.service.ConsultaService;
import hospital.service.FarmaciaService;
import hospital.singleton.SistemaHospitalar;

public class Main {

    public static void main(String[] args) {

        SistemaHospitalar ref1 = SistemaHospitalar.getInstance();
        SistemaHospitalar ref2 = SistemaHospitalar.getInstance();

        System.out.println("=== Verificação do Singleton ===");
        System.out.println("ref1 == ref2? " + (ref1 == ref2)); // sempre true

        ref1.setNomeHospital("Hospital Santa Clara");
        ref1.setCnpj("12.345.678/0001-90");
        ref1.setTotalLeitos(50);

        System.out.println("Hospital : " + ref2.getNomeHospital());
        System.out.println("Leitos   : " + ref2.getTotalLeitos());

        System.out.println("\n=== Início do Plantão ===");
        SistemaHospitalar.getInstance().iniciarPlantao("Dra. Ana Ribeiro");

        ConsultaService consultas = new ConsultaService();
        FarmaciaService farmacia  = new FarmaciaService();

        System.out.println("\n=== Operações Clínicas ===");
        consultas.agendarConsulta("João Silva",    "Cardiologia");
        consultas.realizarConsulta("João Silva",   "Hipertensão leve");
        farmacia.dispensarMedicamento("João Silva","Losartana", "50mg");

        SistemaHospitalar.getInstance().internarPaciente("Maria Souza");
        SistemaHospitalar.getInstance().internarPaciente("Carlos Lima");
        SistemaHospitalar.getInstance().darAlta("Maria Souza");

        System.out.println("\nLeitos disponíveis: "
            + SistemaHospitalar.getInstance().getLeitosDisponiveis()
            + "/" + SistemaHospitalar.getInstance().getTotalLeitos());

        System.out.println("\n=== Encerramento do Plantão ===");
        SistemaHospitalar.getInstance().encerrarPlantao();

        System.out.println("\n=== Log de Eventos Clínicos ===");
        SistemaHospitalar.getInstance()
            .getLogEventos()
            .forEach(System.out::println);
    }
}
