package aniwash.dao;

import aniwash.entity.Appointment;

import java.util.Date;
import java.util.List;

public interface IAppointmentDao {

    // Dao methods

    boolean addAppointment(Appointment appointment);

    List<Appointment> findAllAppointment();

    Appointment findByIdAppointment(Long id);

    Appointment findByDateAppointment(Date date);

    boolean deleteByIdAppointment(Long id);

    boolean updateAppointment(Appointment appointment);

}
