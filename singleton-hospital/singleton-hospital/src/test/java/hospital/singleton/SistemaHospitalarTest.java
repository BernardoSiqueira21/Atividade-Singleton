package hospital.singleton;

import hospital.service.ConsultaService;
import hospital.service.FarmaciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SistemaHospitalar — Padrão Singleton")
class SistemaHospitalarTest {

    @BeforeEach
    void resetarEstado() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        if (s.isSistemaEmOperacao()) {
            s.encerrarPlantao();
        }
        s.setNomeHospital("Hospital Geral");
        s.setTotalLeitos(100);

    }

    @Test
    @DisplayName("getInstance() deve retornar sempre a mesma instância")
    void deveRetornarMesmaInstancia() {
        SistemaHospitalar a = SistemaHospitalar.getInstance();
        SistemaHospitalar b = SistemaHospitalar.getInstance();
        assertSame(a, b);
    }

    @Test
    @DisplayName("Alteração via uma referência deve refletir na outra")
    void alteracaoDeveReflectirEntreReferencias() {
        SistemaHospitalar.getInstance().setNomeHospital("Hospital Teste");
        assertEquals("Hospital Teste", SistemaHospitalar.getInstance().getNomeHospital());
    }

    @Test
    @DisplayName("Deve configurar e recuperar nome do hospital")
    void deveConfigurarNomeHospital() {
        SistemaHospitalar.getInstance().setNomeHospital("Hospital São Lucas");
        assertEquals("Hospital São Lucas", SistemaHospitalar.getInstance().getNomeHospital());
    }

    @Test
    @DisplayName("Deve configurar e recuperar total de leitos")
    void deveConfigurarTotalLeitos() {
        SistemaHospitalar.getInstance().setTotalLeitos(200);
        assertEquals(200, SistemaHospitalar.getInstance().getTotalLeitos());
    }


    @Test
    @DisplayName("Deve iniciar plantão e registrar médico")
    void deveIniciarPlantao() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        s.iniciarPlantao("Dr. Marcos");

        assertTrue(s.isSistemaEmOperacao());
        assertEquals("Dr. Marcos", s.getMedicoPlantao());
    }

    @Test
    @DisplayName("Deve encerrar plantão e limpar médico")
    void deveEncerrarPlantao() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        s.iniciarPlantao("Dr. Marcos");
        s.encerrarPlantao();

        assertFalse(s.isSistemaEmOperacao());
        assertNull(s.getMedicoPlantao());
    }


    @Test
    @DisplayName("Deve internar paciente e reduzir leitos disponíveis")
    void deveInternarPaciente() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        s.iniciarPlantao("Dra. Paula");
        int antes = s.getLeitosDisponiveis();

        s.internarPaciente("Paciente A");

        assertEquals(antes - 1, s.getLeitosDisponiveis());
    }

    @Test
    @DisplayName("Deve dar alta e liberar leito")
    void deveDarAlta() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        s.iniciarPlantao("Dra. Paula");
        s.internarPaciente("Paciente B");
        int antes = s.getLeitosDisponiveis();

        s.darAlta("Paciente B");

        assertEquals(antes + 1, s.getLeitosDisponiveis());
    }

    @Test
    @DisplayName("Deve lançar exceção ao internar sem plantão ativo")
    void deveLancarExcecaoInternarSemPlantao() {
        assertThrows(IllegalStateException.class, () ->
            SistemaHospitalar.getInstance().internarPaciente("Paciente X"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao dar alta sem plantão ativo")
    void deveLancarExcecaoAltaSemPlantao() {
        assertThrows(IllegalStateException.class, () ->
            SistemaHospitalar.getInstance().darAlta("Paciente X"));
    }


    @Test
    @DisplayName("ConsultaService deve lançar exceção sem plantão")
    void consultaServiceDeveLancarExcecaoSemPlantao() {
        assertThrows(IllegalStateException.class, () ->
            new ConsultaService().agendarConsulta("Paciente X", "Ortopedia"));
    }

    @Test
    @DisplayName("FarmaciaService deve registrar evento no log do Singleton")
    void farmaciaServiceDeveRegistrarEvento() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        s.iniciarPlantao("Dr. Felipe");
        int antes = s.getLogEventos().size();

        new FarmaciaService().dispensarMedicamento("Maria", "Dipirona", "500mg");

        assertTrue(s.getLogEventos().size() > antes);
    }

    @Test
    @DisplayName("Log deve conter eventos registrados")
    void deveRegistrarEventosNoLog() {
        SistemaHospitalar s = SistemaHospitalar.getInstance();
        int antes = s.getLogEventos().size();

        s.registrarEvento("Evento de teste hospitalar");

        assertEquals(antes + 1, s.getLogEventos().size());
        assertTrue(s.getLogEventos().get(s.getLogEventos().size() - 1)
                .contains("Evento de teste hospitalar"));
    }
}
