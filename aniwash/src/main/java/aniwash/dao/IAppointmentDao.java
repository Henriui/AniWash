package aniwash.dao;

import aniwash.entity.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAppointmentDao extends IDao {

    boolean addAppointment(Appointment appointment);

    List<Appointment> fetchAppointments();

    List<Appointment> findAllAppointments();

    Appointment findByIdAppointment(Long id);

    Appointment findByStartDateAppointment(ZonedDateTime date);

    boolean deleteByIdAppointment(Long id);

    boolean updateAppointment(Appointment appointment);

    Appointment findNewestAppointment();

}
