/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ucionchology.api.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ucionchology.models.DayDrugDosage;
import org.openmrs.module.ucionchology.models.PatientProtocol;
import org.openmrs.module.ucionchology.models.Phase;
import org.openmrs.module.ucionchology.models.Protocol;
import org.openmrs.module.ucionchology.models.StageDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Query;

@Repository("ucionchology.UCIOnchologyDao")
public class UCIOnchologyDao {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Protocol saveOrUpdateProtocal(Protocol protocal) throws APIException {
		getSession().saveOrUpdate(protocal);
		return protocal;
	}
	
	public Protocol getProtocalById(int protocalId) throws APIException {
		return (Protocol) getSession().get(Protocol.class, protocalId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Protocol> getAllProtocals() throws APIException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Protocol.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public void deleteProtocal(Protocol protocal) throws APIException {
		getSession().delete(protocal);
	}
	
	
	public Phase saveOrUpdatePhase(Phase phase) throws APIException {
		getSession().saveOrUpdate(phase);
		return phase;
	}
	
	public Phase getPhaseById(int phaseId) throws APIException {
		return (Phase) getSession().get(Phase.class, phaseId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Phase> getAllphases() throws APIException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Phase.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public void deletePhase(Phase phase) throws APIException {
		getSession().delete(phase);
	}
	
	public Phase voidPhase(Phase phase) throws APIException {
		phase.setVoided(true);
		getSession().saveOrUpdate(phase);
		return phase;
	}
	
	public StageDay saveOrUpdateStageDay(StageDay stageDay) throws APIException {
		getSession().saveOrUpdate(stageDay);
		return stageDay;
	}
	
	public StageDay getStageDayById(int stageDayId) throws APIException {
		return (StageDay) getSession().get(StageDay.class, stageDayId);
	}
	
	@SuppressWarnings("unchecked")
	public List<StageDay> getAllStageDay() throws APIException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(StageDay.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public void deleteStageDay(StageDay stageDay) throws APIException {
		getSession().delete(stageDay);
		
	}
	
	
	public DayDrugDosage saveOrUpdateDayDrugDosage(DayDrugDosage drugDayDose) throws APIException {
		getSession().saveOrUpdate(drugDayDose);
		return drugDayDose;
	}
	
	public DayDrugDosage getDayDrugDosageById(int drugDayDoseId) throws APIException {
		return (DayDrugDosage) getSession().get(DayDrugDosage.class, drugDayDoseId);
	}
	
	@SuppressWarnings("unchecked")
	public List<DayDrugDosage> getAllDayDrugDosage() throws APIException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DayDrugDosage.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public void deleteDayDrugDosage(DayDrugDosage drugDayDose) throws APIException {
		getSession().delete(drugDayDose);
	}
	
	
	public PatientProtocol saveOrUpdatePatientProtocol(PatientProtocol patientProtocal) throws APIException {
		getSession().saveOrUpdate(patientProtocal);
		return patientProtocal;
	}
	
	public PatientProtocol getDayPatientProtocolById(int patientProtocalId) throws APIException {
		return (PatientProtocol) getSession().get(PatientProtocol.class, patientProtocalId);
	}
	
	@SuppressWarnings("unchecked")
	public List<PatientProtocol> getAllPatientProtocol() throws APIException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientProtocol.class);
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	public void deletePatientProtocol(PatientProtocol patientProtocal) throws APIException {
		getSession().delete(patientProtocal);
	}
	
	@SuppressWarnings("unchecked")
	public List<Patient> getPatienstByProtocal(Protocol protocal) throws APIException {
		String hql = "SELECT patientId FROM PatientProtocol WHERE protocalId = :protocalId";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("protocalId", protocal.getId());
		
		List<Integer> patientIds = (List<Integer>) query.list();
		
		List<Patient> patients = new ArrayList<Patient>();
		for (int id : patientIds) {
			patients.add(Context.getPatientService().getPatient(id));
		}
		return patients;
	}
	
	public List<Patient> getPatienstByDate(Date date) throws APIException {
		String hql = "SELECT patientId FROM PatientProtocol WHERE :date BETWEEN dateStarted AND dateStopped";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setTimestamp("date", date);
		
		List<Integer> patientIds = (List<Integer>) query.list();
		
		List<Patient> patients = new ArrayList<Patient>();
		for (int id : patientIds) {
			patients.add(Context.getPatientService().getPatient(id));
		}
		return patients;
	}
	
	public Protocol getPatientCurrentProtocal(int patientId) throws APIException {
		String hql = "SELECT protocalId FROM PatientProtocol WHERE  patientId = :patientId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("patientId", patientId);
		int protocaolId = (Integer) query.uniqueResult();
		return getProtocalById(protocaolId);
	}
}
