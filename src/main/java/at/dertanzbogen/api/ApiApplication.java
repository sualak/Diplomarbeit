package at.dertanzbogen.api;

import at.dertanzbogen.api.Fakers.CourseFaker;
import at.dertanzbogen.api.Fakers.UserFaker;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.NotificationRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.CourseAdminController;
import at.dertanzbogen.api.presentation.CourseUserController;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import at.dertanzbogen.api.presentation.UserRegistrationController;
import at.dertanzbogen.api.service.CourseAdminService;
import at.dertanzbogen.api.service.CourseUserService;
import at.dertanzbogen.api.service.NotificationUserService;
import at.dertanzbogen.api.service.UserService;
import at.dertanzbogen.api.util.EmailExtractorHelper;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

//(exclude = {SecurityAutoConfiguration.class})

@SpringBootApplication
public class ApiApplication {

	@Value("${stripe.api.key}")
	private String stripeApiKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeApiKey;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(UserRepository userRepository,
								 UserRegistrationController userRegistrationController,
								 UserService userService,
								 CourseRepository courseRepository,
								 CourseUserController courseUserController,
								 LoggerMailSenderImpl loggerMailSender,
								 CourseUserService courseUserService,
								 CourseAdminController courseAdminController,
								 CourseAdminService courseAdminService,
								 NotificationUserService notificationUserService,
								 NotificationRepository notificationRepository)
	{

		System.out.println("CommandLineRunner");
		return args -> {

			userRepository.deleteAll();
			courseRepository.deleteAll();
			notificationRepository.deleteAll();List<UserRegistrationCommand> usersStatic = UserFaker.generateStaticUsersDTO();
			List<UserRegistrationCommand> usersDynamic = UserFaker.generateUsersDTO(10);
			List<Commands.CourseCreationCommand> courseCreationCommands = CourseFaker.generateCoursesCommandOfAllTypes(5);

			courseCreationCommands.forEach(courseAdminController::createCourse);
//

			usersStatic.forEach(user -> {
				String location = userRegistrationController.register(user).getHeaders().get("Location").get(0);
				String id = location.substring(location.lastIndexOf("/") + 1);
				userRegistrationController.verify(new Commands.VerificationCommand(id, EmailExtractorHelper.extractTokenId(loggerMailSender)));
			});

			usersDynamic.forEach(user -> {
				String location = userRegistrationController.register(user).getHeaders().get("Location").get(0);
				String id = location.substring(location.lastIndexOf("/") + 1);
				userRegistrationController.verify(new Commands.VerificationCommand(id, EmailExtractorHelper.extractTokenId(loggerMailSender)));
			});

			Course course = courseRepository.findCoursesByCourseStatus(CourseBaseEntity.CourseStatus.OPEN).get(0);

			course.addTeacher(userRepository.findByEmailEmail("teacher.piffel@gmx.at").get().getId());

			Course save = courseRepository.save(course);

			userRepository.findAll().forEach(user -> {
				courseUserService.bookCourse(user, save.getId(), user.isLeader());
			});

			Pageable pageable = PageRequest.of(0, 10);
			User student = userRepository.findByEmailEmail("student.piffel@gmx.at").get();
			User admin = userRepository.findByEmailEmail("admin.piffel@gmx.at").get();
			userService.addPartner(student, new Commands.PartnerRequestCommand("admin.piffel@gmx.at"));
			notificationUserService.acceptNotification(admin, notificationRepository.findNotificationByCreatorId(student.getId(),pageable).getContent().get(0).getId());


		};
	}
}
