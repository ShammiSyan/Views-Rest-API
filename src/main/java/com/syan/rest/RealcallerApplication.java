package com.syan.rest;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.syan.rest.cli.RenderCommand;
import com.syan.rest.dao.ProfileDataDAO;
import com.syan.rest.dao.ProfileViewDAO;
import com.syan.rest.core.ProfileData;
import com.syan.rest.core.ProfileView;
import com.syan.rest.health.TemplateHealthCheck;
import com.syan.rest.resources.ProfileViewResource;
import com.syan.rest.RealcallerConfiguration;
import com.xeiam.dropwizard.sundial.SundialBundle;
import com.xeiam.dropwizard.sundial.SundialConfiguration;
import com.xeiam.dropwizard.sundial.tasks.AddCronJobTriggerTask;
import com.xeiam.dropwizard.sundial.tasks.AddJobTask;
import com.xeiam.dropwizard.sundial.tasks.LockSundialSchedulerTask;
import com.xeiam.dropwizard.sundial.tasks.RemoveJobTask;
import com.xeiam.dropwizard.sundial.tasks.RemoveJobTriggerTask;
import com.xeiam.dropwizard.sundial.tasks.StartJobTask;
import com.xeiam.dropwizard.sundial.tasks.StopJobTask;
import com.xeiam.dropwizard.sundial.tasks.UnlockSundialSchedulerTask;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Main Application class which has all the bundles and commands 
 * providing the basic functionality.
 * 
 * @author prsyan
 *
 */
public class RealcallerApplication extends Application<RealcallerConfiguration> {
	
	/** Reference for the logger object. **/
	private static final Logger log = LoggerFactory
			.getLogger(RealcallerApplication.class);

	private final HibernateBundle<RealcallerConfiguration> hibernateBundle = new HibernateBundle<RealcallerConfiguration>(
			ProfileData.class, ProfileView.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(
				RealcallerConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public void initialize(Bootstrap<RealcallerConfiguration> bootstrap) {
		bootstrap.addBundle(new SundialBundle<RealcallerConfiguration>() {
			@Override
			public SundialConfiguration getSundialConfiguration(
					RealcallerConfiguration configuration) {
				return configuration.getSundialConfiguration();
			}

			@Override
			public void initialize(Bootstrap bootstrap) {
			}
		});

		bootstrap.addCommand(new RenderCommand());
		bootstrap.addBundle(new MigrationsBundle<RealcallerConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(
					RealcallerConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addBundle(hibernateBundle);

	}

	@Override
	public void run(RealcallerConfiguration configuration,
			Environment environment) throws Exception {

		log.info("Method Realcaller Application run() called");
		for (int i = 0; i < configuration.getMessageRepetitions(); i++) {
			System.out.println(configuration.getMessage());
		}
		final ProfileViewDAO userProfileViewDAO = new ProfileViewDAO(
				hibernateBundle.getSessionFactory());
		final ProfileDataDAO userProfileDataDAO = new ProfileDataDAO(
				hibernateBundle.getSessionFactory());
		final TemplateHealthCheck templatehealthCheck = new TemplateHealthCheck(
				configuration.getTemplate());
		SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
		environment.jersey().register(
				new ProfileViewResource(userProfileDataDAO,
						userProfileViewDAO));
		environment.getObjectMapper().configure(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		environment.healthChecks().register("template", templatehealthCheck);
		environment.getApplicationContext().setAttribute("sessionFactory",
				sessionFactory);
		environment.admin().addTask(new StartJobTask());
		environment.admin().addTask(new StopJobTask());
		environment.admin().addTask(new RemoveJobTask());
		environment.admin().addTask(new AddJobTask());
		environment.admin().addTask(new RemoveJobTriggerTask());
		environment.admin().addTask(new AddCronJobTriggerTask());
		environment.admin().addTask(new LockSundialSchedulerTask());
		environment.admin().addTask(new UnlockSundialSchedulerTask());
	}

	public static void main(String[] args) throws Exception {
		new RealcallerApplication().run(args);
	}

}
