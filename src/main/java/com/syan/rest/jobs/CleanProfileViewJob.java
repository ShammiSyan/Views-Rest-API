package com.syan.rest.jobs;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syan.rest.dao.ProfileViewDAO;
import com.syan.rest.util.ProfileConst;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

/**
 * Fires the job daily at midnight(11:00 pm) to delete the older Views.
 *
 */
@CronTrigger(cron = "0 0 23 * * ?")
public class CleanProfileViewJob extends Job {

	/** Reference for the logger object. **/
	private final Logger log = LoggerFactory
			.getLogger(CleanProfileViewJob.class);

	@Override
	public void doRun() throws JobInterruptException {

		log.info(" In the doRun() method.");
		log.info(" Getting the sessionfactory Object.");
		SessionFactory sessionFactory = (SessionFactory) SundialJobScheduler
				.getServletContext().getAttribute("sessionFactory");
		ProfileViewDAO profileViewDAO = new ProfileViewDAO(
				sessionFactory);
		log.info(" Opening a Session.");
		Session session = sessionFactory.openSession();

		try {
			ManagedSessionContext.bind(session);
			Transaction transaction = session.beginTransaction();
			try {
				Timestamp dayLimit = Timestamp.valueOf(LocalDateTime.now()
						.minusDays(ProfileConst.DEFUALT_MAX_DAYS_LIMIT));
				log.info("Time to 10 Days from now" + dayLimit);
				profileViewDAO.deleteOldProfileView(dayLimit);
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				log.error(" Rolled back the transaction. Some exception occured.");
				throw new RuntimeException(e);
			}
		} finally {
			session.close();
			log.info(" Closed the Session");
			ManagedSessionContext.unbind(sessionFactory);
		}

	}

}
