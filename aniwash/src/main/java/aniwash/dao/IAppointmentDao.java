package aniwash.dao;

import aniwash.entity.Appointment;

import java.util.Date;
import java.util.List;

public interface IAppointmentDao {
    boolean addAppointment(Appointment appointment);

    List<Appointment> findAllAppointment();

    Appointment findByIdAppointment(Long id);

    Appointment findByDateAppointment(Date date);

/*  TODO: Implement these methods
    Appointment findByEmployeeAppointment(Employee employee);

    Appointment findByCustomerAppointment(Customer customer);

    Appointment findByAnimalAppointment(Animal animal);

    Appointment findByProductAppointment(Product product);
*/

    boolean deleteByIdAppointment(Long id);

    boolean updateAppointment(Appointment appointment);

}
