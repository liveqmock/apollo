package cn.com.youtong.apollo.analyse.form;

import java.io.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

public class ScalarTemplateResultForm {

	/**
	 * ����
	 */
	private Task task;

	/**
	 * ��λ����
	 */
	private UnitTreeNode[] units;

	/**
	 * ָ����������������
	 */
	private TaskTime[] taskTimes;

	/**
	 * ָ������
	 */
	private ScalarForm[] scalars;

	/** �滻 */
	private ScalarQueryTemplateForm tplForm;
	public ScalarTemplateResultForm( Task task, UnitTreeNode[] units,
									 TaskTime[] taskTimes, ScalarForm[] scalars,
									 ScalarQueryTemplateForm tplForm ) {
		this.task = task;
		this.units = units;
		this.taskTimes = taskTimes;
		this.scalars = scalars;
		this.tplForm = tplForm;
	}

	public ScalarForm[] getScalars() {
		return scalars;
	}

	public Task getTask() {
		return task;
	}

	public TaskTime[] getTaskTimes() {
		return taskTimes;
	}

	public ScalarQueryTemplateForm getTplForm() {
		return tplForm;
	}

	public UnitTreeNode[] getUnits() {
		return units;
	}

}