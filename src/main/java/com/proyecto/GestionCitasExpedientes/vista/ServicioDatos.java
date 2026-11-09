package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.Servicio.*;
import com.proyecto.GestionCitasExpedientes.modelo.*;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servicio unificado de datos para la interfaz JavaFX.
 * Conecta con los servicios de Spring Data JPA si la base de datos está disponible;
 * en caso contrario, provee almacenamiento reactivo en memoria con datos semilla
 * para garantizar la ejecución fluida e interactiva de todas las pantallas.
 */
public class ServicioDatos {

    private static ServicioDatos instancia;

    private ApplicationContext springContext;
    private boolean modoBaseDatos = false;

    // Usuario autenticado actual
    private String usuarioActual = "Administrador";
    private String rolActual = "Administrador";

    // Almacenamiento en memoria (Modo Local / Fallback)
    private final List<Especialidad> especialidadesMemoria = new ArrayList<>();
    private final List<Medico> medicosMemoria = new ArrayList<>();
    private final List<Paciente> pacientesMemoria = new ArrayList<>();
    private final List<ExpedienteMedico> expedientesMemoria = new ArrayList<>();
    private final List<Cita> citasMemoria = new ArrayList<>();
    private final List<Consulta> consultasMemoria = new ArrayList<>();

    private final AtomicInteger seqEspecialidad = new AtomicInteger(10);
    private final AtomicInteger seqPersona = new AtomicInteger(10);
    private final AtomicInteger seqExpediente = new AtomicInteger(10);
    private final AtomicInteger seqCita = new AtomicInteger(10);
    private final AtomicInteger seqConsulta = new AtomicInteger(10);

    private ServicioDatos() {
        inicializarDatosSemillaMemoria();
    }

    public static synchronized ServicioDatos getInstancia() {
        if (instancia == null) {
            instancia = new ServicioDatos();
        }
        return instancia;
    }

    public void inicializarConSpring(ApplicationContext context) {
        if (context != null) {
            this.springContext = context;
            try {
                // Verificar que los repositorios respondan
                PacienteServicio ps = context.getBean(PacienteServicio.class);
                ps.listar();
                this.modoBaseDatos = true;
                System.out.println("ServicioDatos: Conectado exitosamente a Spring Data JPA y MySQL.");
            } catch (Exception e) {
                System.err.println("ServicioDatos: MySQL no disponible (" + e.getMessage() + "). Operando en modo memoria local.");
                this.modoBaseDatos = false;
            }
        }
    }

    public boolean isModoBaseDatos() {
        return modoBaseDatos;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public String getRolActual() {
        return rolActual;
    }

    public void setRolActual(String rolActual) {
        this.rolActual = rolActual;
    }

    // =========================================================================
    // DATOS SEMILLA EN MEMORIA
    // =========================================================================
    private void inicializarDatosSemillaMemoria() {
        // 1. Especialidades
        Especialidad espGen = new Especialidad();
        espGen.setIdEspecialidad(1);
        espGen.setNombre("Medicina General");
        espGen.setDescripcion("Atención médica primaria y preventiva");
        espGen.setCostoConsulta(75.0);
        especialidadesMemoria.add(espGen);

        Especialidad espPed = new Especialidad();
        espPed.setIdEspecialidad(2);
        espPed.setNombre("Pediatría");
        espPed.setDescripcion("Atención médica integral infantil");
        espPed.setCostoConsulta(100.0);
        especialidadesMemoria.add(espPed);

        Especialidad espCard = new Especialidad();
        espCard.setIdEspecialidad(3);
        espCard.setNombre("Cardiología");
        espCard.setDescripcion("Diagnóstico y tratamiento cardiovascular");
        espCard.setCostoConsulta(150.0);
        especialidadesMemoria.add(espCard);

        Especialidad espTrauma = new Especialidad();
        espTrauma.setIdEspecialidad(4);
        espTrauma.setNombre("Traumatología");
        espTrauma.setDescripcion("Lesiones del aparato locomotor y óseo");
        espTrauma.setCostoConsulta(125.0);
        especialidadesMemoria.add(espTrauma);

        // 2. Médicos
        Medico m1 = new Medico();
        m1.setIdPersona(1);
        m1.setNombre("Dr. Carlos Mendoza");
        m1.setEmail("carlos.mendoza@clinica.com");
        m1.setTelefono("5521-4321");
        m1.setDireccion("Zona 10, Edificio Médico Las Américas");
        m1.setColegiado("COL-1045");
        m1.setHorarioAtencion("08:00-12:00;14:00-18:00");
        m1.setEspecialidad(espGen);
        medicosMemoria.add(m1);

        Medico m2 = new Medico();
        m2.setIdPersona(2);
        m2.setNombre("Dra. Ana Sofía Ruiz");
        m2.setEmail("ana.ruiz@clinica.com");
        m2.setTelefono("5533-8765");
        m2.setDireccion("Zona 14, Clínica Pediátrica San José");
        m2.setColegiado("COL-2099");
        m2.setHorarioAtencion("08:00-13:00;15:00-19:00");
        m2.setEspecialidad(espPed);
        medicosMemoria.add(m2);

        Medico m3 = new Medico();
        m3.setIdPersona(3);
        m3.setNombre("Dr. Roberto Gómez");
        m3.setEmail("roberto.gomez@clinica.com");
        m3.setTelefono("5544-9988");
        m3.setDireccion("Zona 9, Centro Cardiovascular");
        m3.setColegiado("COL-3110");
        m3.setHorarioAtencion("09:00-13:00;14:00-17:00");
        m3.setEspecialidad(espCard);
        medicosMemoria.add(m3);

        // 3. Pacientes y Expedientes
        Paciente p1 = new Paciente();
        p1.setIdPersona(4);
        p1.setNombre("Juan Carlos Pérez");
        p1.setEmail("juan.perez@example.com");
        p1.setTelefono("4432-1100");
        p1.setDireccion("Zona 2, 1-26 San Pedro");
        p1.setEdad(34);
        p1.setGenero("Masculino");
        p1.setTipoSangre("O+");
        p1.setHistorial("Hipertensión controlada en familia");
        ExpedienteMedico exp1 = new ExpedienteMedico();
        exp1.setIdExpediente(1);
        exp1.setFechaCreacion(LocalDate.now().minusMonths(6));
        exp1.setAntecedente(p1.getHistorial());
        exp1.setPaciente(p1);
        p1.setExpediente(exp1);
        pacientesMemoria.add(p1);
        expedientesMemoria.add(exp1);

        Paciente p2 = new Paciente();
        p2.setIdPersona(5);
        p2.setNombre("María Elena Morales");
        p2.setEmail("maria.morales@example.com");
        p2.setTelefono("4411-2233");
        p2.setDireccion("Zona 11, Colonia Mariscal");
        p2.setEdad(28);
        p2.setGenero("Femenino");
        p2.setTipoSangre("A+");
        p2.setHistorial("Sin antecedentes crónicos");
        ExpedienteMedico exp2 = new ExpedienteMedico();
        exp2.setIdExpediente(2);
        exp2.setFechaCreacion(LocalDate.now().minusMonths(3));
        exp2.setAntecedente(p2.getHistorial());
        exp2.setPaciente(p2);
        p2.setExpediente(exp2);
        pacientesMemoria.add(p2);
        expedientesMemoria.add(exp2);

        Paciente p3 = new Paciente();
        p3.setIdPersona(6);
        p3.setNombre("Pedro Luis Ramírez");
        p3.setEmail("pedro.ramirez@example.com");
        p3.setTelefono("4455-6677");
        p3.setDireccion("Zona 7, Quinta Samayoa");
        p3.setEdad(45);
        p3.setGenero("Masculino");
        p3.setTipoSangre("B+");
        p3.setHistorial("Alergia a la penicilina");
        ExpedienteMedico exp3 = new ExpedienteMedico();
        exp3.setIdExpediente(3);
        exp3.setFechaCreacion(LocalDate.now().minusMonths(1));
        exp3.setAntecedente(p3.getHistorial());
        exp3.setPaciente(p3);
        p3.setExpediente(exp3);
        pacientesMemoria.add(p3);
        expedientesMemoria.add(exp3);

        Paciente p4 = new Paciente();
        p4.setIdPersona(7);
        p4.setNombre("Laura Patricia Castillo");
        p4.setEmail("laura.castillo@example.com");
        p4.setTelefono("4488-9900");
        p4.setDireccion("Zona 15, Vista Hermosa");
        p4.setEdad(22);
        p4.setGenero("Femenino");
        p4.setTipoSangre("O-");
        p4.setHistorial("Asma leve estacional");
        ExpedienteMedico exp4 = new ExpedienteMedico();
        exp4.setIdExpediente(4);
        exp4.setFechaCreacion(LocalDate.now());
        exp4.setAntecedente(p4.getHistorial());
        exp4.setPaciente(p4);
        p4.setExpediente(exp4);
        pacientesMemoria.add(p4);
        expedientesMemoria.add(exp4);

        // 4. Citas
        Cita c1 = new Cita();
        c1.setIdCita(1);
        c1.setPaciente(p1);
        c1.setMedico(m1);
        c1.setFecha(LocalDate.now());
        c1.setHora(LocalTime.of(9, 0));
        c1.setConfirmada(true);
        c1.setCosto(espGen.getCostoConsulta());
        citasMemoria.add(c1);

        Cita c2 = new Cita();
        c2.setIdCita(2);
        c2.setPaciente(p2);
        c2.setMedico(m2);
        c2.setFecha(LocalDate.now());
        c2.setHora(LocalTime.of(10, 30));
        c2.setConfirmada(true);
        c2.setCosto(espPed.getCostoConsulta());
        citasMemoria.add(c2);

        Cita c3 = new Cita();
        c3.setIdCita(3);
        c3.setPaciente(p3);
        c3.setMedico(m3);
        c3.setFecha(LocalDate.now().plusDays(1));
        c3.setHora(LocalTime.of(11, 0));
        c3.setConfirmada(true);
        c3.setCosto(espCard.getCostoConsulta());
        citasMemoria.add(c3);

        Cita c4 = new Cita();
        c4.setIdCita(4);
        c4.setPaciente(p4);
        c4.setMedico(m1);
        c4.setFecha(LocalDate.now().plusDays(2));
        c4.setHora(LocalTime.of(15, 0));
        c4.setConfirmada(false);
        c4.setMotivoCancelacion("Reprogramación por viaje (cancelado por: Paciente)");
        c4.setCosto(espGen.getCostoConsulta());
        citasMemoria.add(c4);

        // 5. Consultas
        Consulta con1 = new Consulta();
        con1.setIdConsulta(1);
        con1.setCita(c1);
        con1.setExpediente(exp1);
        con1.setFechaConsulta(LocalDateTime.now().minusDays(10));
        con1.setSintomas("Cefalea intensa constante de 3 días, fotofobia leve");
        con1.setDiagnostico("Cefalea tensional por fatiga visual");
        con1.setTratamiento("Paracetamol 500mg c/8h por 3 días. Descanso y evaluación oftalmológica");
        consultasMemoria.add(con1);
        exp1.getConsultas().add(con1);
    }

    // =========================================================================
    // PACIENTES Y EXPEDIENTES
    // =========================================================================
    public List<Paciente> listarPacientes() {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(PacienteServicio.class).listar();
            } catch (Exception e) {
                System.err.println("Error BD listarPacientes: " + e.getMessage());
            }
        }
        return new ArrayList<>(pacientesMemoria);
    }

    public Paciente guardarPaciente(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio.");
        }
        if (paciente.getEmail() == null || paciente.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }

        if (modoBaseDatos) {
            try {
                PacienteServicio ps = springContext.getBean(PacienteServicio.class);
                if (paciente.getIdPersona() != null && paciente.getIdPersona() > 0) {
                    return ps.actualizar(paciente.getIdPersona(), paciente);
                } else {
                    return ps.registrar(paciente);
                }
            } catch (Exception e) {
                System.err.println("Error BD guardarPaciente: " + e.getMessage());
            }
        }

        // Modo Memoria
        if (paciente.getIdPersona() != null && paciente.getIdPersona() > 0) {
            for (int i = 0; i < pacientesMemoria.size(); i++) {
                if (pacientesMemoria.get(i).getIdPersona().equals(paciente.getIdPersona())) {
                    pacientesMemoria.set(i, paciente);
                    if (paciente.getExpediente() != null) {
                        paciente.getExpediente().setAntecedente(paciente.getHistorial());
                    }
                    return paciente;
                }
            }
        }

        // Registro nuevo
        for (Paciente p : pacientesMemoria) {
            if (p.getEmail() != null && p.getEmail().equalsIgnoreCase(paciente.getEmail())) {
                throw new IllegalArgumentException("Ya existe un paciente con el correo: " + paciente.getEmail());
            }
        }
        paciente.setIdPersona(seqPersona.incrementAndGet());
        ExpedienteMedico exp = new ExpedienteMedico();
        exp.setIdExpediente(seqExpediente.incrementAndGet());
        exp.setFechaCreacion(LocalDate.now());
        exp.setAntecedente(paciente.getHistorial());
        exp.setPaciente(paciente);
        paciente.setExpediente(exp);

        pacientesMemoria.add(paciente);
        expedientesMemoria.add(exp);
        return paciente;
    }

    public void eliminarPaciente(Integer id) {
        if (modoBaseDatos) {
            try {
                springContext.getBean(PacienteServicio.class).eliminar(id);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // Validar si tiene consultas asociadas
        for (Consulta c : consultasMemoria) {
            if (c.getExpediente() != null && c.getExpediente().getPaciente() != null
                    && id.equals(c.getExpediente().getPaciente().getIdPersona())) {
                throw new RuntimeException("No se puede eliminar un paciente con consultas médicas registradas.");
            }
        }

        pacientesMemoria.removeIf(p -> id.equals(p.getIdPersona()));
        citasMemoria.removeIf(c -> c.getPaciente() != null && id.equals(c.getPaciente().getIdPersona()));
    }

    // =========================================================================
    // MÉDICOS
    // =========================================================================
    public List<Medico> listarMedicos() {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(MedicoServicio.class).listar();
            } catch (Exception e) {
                System.err.println("Error BD listarMedicos: " + e.getMessage());
            }
        }
        return new ArrayList<>(medicosMemoria);
    }

    public Medico guardarMedico(Medico medico) {
        if (medico.getNombre() == null || medico.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del médico es obligatorio.");
        }
        if (medico.getColegiado() == null || medico.getColegiado().isBlank()) {
            throw new IllegalArgumentException("El número de colegiado es obligatorio.");
        }
        if (medico.getEspecialidad() == null) {
            throw new IllegalArgumentException("Debe seleccionar una especialidad.");
        }

        if (modoBaseDatos) {
            try {
                MedicoServicio ms = springContext.getBean(MedicoServicio.class);
                if (medico.getIdPersona() != null && medico.getIdPersona() > 0) {
                    return ms.actualizar(medico.getIdPersona(), medico);
                } else {
                    return ms.registrar(medico);
                }
            } catch (Exception e) {
                System.err.println("Error BD guardarMedico: " + e.getMessage());
            }
        }

        // Memoria
        if (medico.getIdPersona() != null && medico.getIdPersona() > 0) {
            for (int i = 0; i < medicosMemoria.size(); i++) {
                if (medicosMemoria.get(i).getIdPersona().equals(medico.getIdPersona())) {
                    medicosMemoria.set(i, medico);
                    return medico;
                }
            }
        }

        for (Medico m : medicosMemoria) {
            if (m.getColegiado() != null && m.getColegiado().equalsIgnoreCase(medico.getColegiado())) {
                throw new IllegalArgumentException("El número de colegiado ya se encuentra registrado.");
            }
        }
        medico.setIdPersona(seqPersona.incrementAndGet());
        medicosMemoria.add(medico);
        return medico;
    }

    public void eliminarMedico(Integer id) {
        if (modoBaseDatos) {
            try {
                springContext.getBean(MedicoServicio.class).eliminar(id);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        medicosMemoria.removeIf(m -> id.equals(m.getIdPersona()));
    }

    // =========================================================================
    // ESPECIALIDADES
    // =========================================================================
    public List<Especialidad> listarEspecialidades() {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(EspecialidadServicio.class).listar();
            } catch (Exception e) {
                System.err.println("Error BD listarEspecialidades: " + e.getMessage());
            }
        }
        return new ArrayList<>(especialidadesMemoria);
    }

    public Especialidad guardarEspecialidad(Especialidad esp) {
        if (esp.getNombre() == null || esp.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        }
        if (esp.getCostoConsulta() < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }

        if (modoBaseDatos) {
            try {
                EspecialidadServicio es = springContext.getBean(EspecialidadServicio.class);
                if (esp.getIdEspecialidad() != null && esp.getIdEspecialidad() > 0) {
                    return es.actualizar(esp.getIdEspecialidad(), esp);
                } else {
                    return es.crear(esp);
                }
            } catch (Exception e) {
                System.err.println("Error BD guardarEspecialidad: " + e.getMessage());
            }
        }

        if (esp.getIdEspecialidad() != null && esp.getIdEspecialidad() > 0) {
            for (int i = 0; i < especialidadesMemoria.size(); i++) {
                if (especialidadesMemoria.get(i).getIdEspecialidad().equals(esp.getIdEspecialidad())) {
                    especialidadesMemoria.set(i, esp);
                    return esp;
                }
            }
        }

        esp.setIdEspecialidad(seqEspecialidad.incrementAndGet());
        especialidadesMemoria.add(esp);
        return esp;
    }

    public void eliminarEspecialidad(Integer id) {
        if (modoBaseDatos) {
            try {
                springContext.getBean(EspecialidadServicio.class).eliminar(id);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        for (Medico m : medicosMemoria) {
            if (m.getEspecialidad() != null && id.equals(m.getEspecialidad().getIdEspecialidad())) {
                throw new RuntimeException("No se puede eliminar la especialidad porque tiene médicos asignados.");
            }
        }
        especialidadesMemoria.removeIf(e -> id.equals(e.getIdEspecialidad()));
    }

    // =========================================================================
    // CITAS
    // =========================================================================
    public List<Cita> listarCitas() {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(CitaServicio.class).listar();
            } catch (Exception e) {
                System.err.println("Error BD listarCitas: " + e.getMessage());
            }
        }
        return new ArrayList<>(citasMemoria);
    }

    public Cita programarCita(Cita c) {
        if (c.getFecha() == null || c.getHora() == null) {
            throw new IllegalArgumentException("Debe seleccionar fecha y hora para la cita.");
        }
        if (c.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser anterior a hoy.");
        }
        if (c.getPaciente() == null) {
            throw new IllegalArgumentException("Debe seleccionar un paciente.");
        }
        if (c.getMedico() == null) {
            throw new IllegalArgumentException("Debe seleccionar un médico.");
        }

        if (modoBaseDatos) {
            try {
                return springContext.getBean(CitaServicio.class).programarCita(c);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // Validación de duplicidad en memoria
        for (Cita exist : citasMemoria) {
            if (exist.getConfirmada() && exist.getMedico().getIdPersona().equals(c.getMedico().getIdPersona())
                    && exist.getFecha().equals(c.getFecha()) && exist.getHora().equals(c.getHora())) {
                throw new IllegalArgumentException("El médico ya tiene una cita asignada en ese horario exacto.");
            }
            if (exist.getConfirmada() && exist.getPaciente().getIdPersona().equals(c.getPaciente().getIdPersona())
                    && exist.getMedico().getIdPersona().equals(c.getMedico().getIdPersona())
                    && exist.getFecha().equals(c.getFecha())) {
                throw new IllegalArgumentException("El paciente ya cuenta con una cita activa con este médico en la misma fecha.");
            }
        }

        c.setIdCita(seqCita.incrementAndGet());
        c.setConfirmada(true);
        if (c.getMedico().getEspecialidad() != null) {
            c.setCosto(c.getMedico().getEspecialidad().getCostoConsulta());
        } else {
            c.setCosto(50.0);
        }

        citasMemoria.add(c);
        return c;
    }

    public Cita confirmarCita(Integer idCita) {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(CitaServicio.class).confirmarCita(idCita);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        for (Cita c : citasMemoria) {
            if (idCita.equals(c.getIdCita())) {
                c.setConfirmada(true);
                c.setMotivoCancelacion(null);
                return c;
            }
        }
        throw new IllegalArgumentException("Cita no encontrada.");
    }

    public Cita cancelarCita(Integer idCita, String motivo, String quien) {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(CitaServicio.class).cancelarCita(idCita, motivo, quien);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        for (Cita c : citasMemoria) {
            if (idCita.equals(c.getIdCita())) {
                c.setConfirmada(false);
                c.setMotivoCancelacion(motivo + " (cancelado por: " + quien + ")");
                return c;
            }
        }
        throw new IllegalArgumentException("Cita no encontrada.");
    }

    // =========================================================================
    // CONSULTAS Y EXPEDIENTES
    // =========================================================================
    public List<Consulta> listarConsultas() {
        if (modoBaseDatos) {
            try {
                return springContext.getBean(ConsultaServicio.class).listar();
            } catch (Exception e) {
                System.err.println("Error BD listarConsultas: " + e.getMessage());
            }
        }
        return new ArrayList<>(consultasMemoria);
    }

    public Consulta registrarConsulta(Consulta consulta) {
        if (consulta.getCita() == null) {
            throw new IllegalArgumentException("Debe seleccionar una cita médica para la consulta.");
        }
        if (consulta.getSintomas() == null || consulta.getSintomas().isBlank()) {
            throw new IllegalArgumentException("Debe registrar los síntomas del paciente.");
        }
        if (consulta.getDiagnostico() == null || consulta.getDiagnostico().isBlank()) {
            throw new IllegalArgumentException("Debe ingresar un diagnóstico clínico.");
        }

        if (modoBaseDatos) {
            try {
                return springContext.getBean(ConsultaServicio.class).registrarConsulta(consulta);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        consulta.setIdConsulta(seqConsulta.incrementAndGet());
        if (consulta.getFechaConsulta() == null) {
            consulta.setFechaConsulta(LocalDateTime.now());
        }

        // Vincular con expediente
        Paciente pac = consulta.getCita().getPaciente();
        ExpedienteMedico exp = pac != null ? pac.getExpediente() : null;
        if (exp == null) {
            exp = new ExpedienteMedico();
            exp.setIdExpediente(seqExpediente.incrementAndGet());
            exp.setFechaCreacion(LocalDate.now());
            exp.setPaciente(pac);
            if (pac != null) pac.setExpediente(exp);
            expedientesMemoria.add(exp);
        }
        consulta.setExpediente(exp);
        exp.getConsultas().add(consulta);
        consultasMemoria.add(consulta);

        return consulta;
    }

    public List<Consulta> listarConsultasPorExpediente(Integer idExpediente) {
        if (idExpediente == null) return Collections.emptyList();
        if (modoBaseDatos) {
            try {
                return springContext.getBean(ConsultaServicio.class).listarPorExpediente(idExpediente);
            } catch (Exception e) {
                System.err.println("Error BD listarConsultasPorExpediente: " + e.getMessage());
            }
        }
        List<Consulta> result = new ArrayList<>();
        for (Consulta c : consultasMemoria) {
            if (c.getExpediente() != null && idExpediente.equals(c.getExpediente().getIdExpediente())) {
                result.add(c);
            }
        }
        return result;
    }

    public ExpedienteMedico buscarExpedientePorPaciente(Integer idPaciente) {
        if (idPaciente == null) return null;
        for (Paciente p : listarPacientes()) {
            if (idPaciente.equals(p.getIdPersona())) {
                return p.getExpediente();
            }
        }
        return null;
    }
}
