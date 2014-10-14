package cn.com.youtong.apollo.analyse.form;

import java.io.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;

public class ScalarTemplateResultForm {

	/**
	 * 任务
	 */
	private Task task;

	/**
	 * 单位数组
	 */
	private UnitTreeNode[] units;

	/**
	 * 指标结果的所属期数组
	 */
	private TaskTime[] taskTimes;

	/**
	 * 指标数组
	 */
	private ScalarForm[] scalars;

	/** 替换 */
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