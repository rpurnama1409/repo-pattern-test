package id.co.sadigit.repopattern.test;

import java.util.List;
import java.util.Optional;

import id.co.sadigit.repopattern.test.dto.StudentDto;
import id.co.sadigit.repopattern.test.entity.Student;
import id.co.sadigit.repopattern.test.repository.StudentRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("students")
public class StudentResource {
    @Inject
    StudentRepository studentRepository;

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public List<StudentDto> getStudents() {
        List<Student> students = studentRepository.listAll();

        return students.stream()
                .map(item -> new StudentDto(item.getId(), item.getName(), item.getMajor())).toList();

    }

    @POST
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")

    public Response storeStudent(StudentDto req) {
        Student student = new Student();
        student.setName(req.name());
        student.setMajor(req.major());

        studentRepository.persist(student);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")

    public Response updateStudent(@PathParam("id") Long id, StudentDto req) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setName(req.name());
            student.setMajor(req.major());

            studentRepository.persist(student);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces("application/json")

    public Response deleteStudent(@PathParam("id") Long id) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setDeleted(true);

            studentRepository.persist(student);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
