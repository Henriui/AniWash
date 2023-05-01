package aniwash.dao;

import aniwash.entity.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAppointmentDao extends IDao<Appointment> {

    Appointment findByStartDate(ZonedDateTime startDate);

    List<Appointment> fetchAppointments();

}
