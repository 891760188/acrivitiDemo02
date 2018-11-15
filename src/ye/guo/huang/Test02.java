package ye.guo.huang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public class Test02 {
	
	public static ProcessEngine processEngine ;
	
	public static void main(String[] args) {
		//获取工作流引擎
		processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
		//部署流程
//		publishProcess();
		
		//启动流程实例
//		startInstance();
		
//		Map<String, Object> variables = new HashMap<String, Object>();
//		variables.put("userId", "请假人");
//		variables.put("suserId", "事权审批人");
//		variables.put("cuserId", "财权审批人");
//		startInstanceVariables(variables);
		
		//获取我的任务
//		queryMyTask("张三1");
		
		//完成当前任务
//		complete("100008");
		
		//可以分配个人任务从一个人到另一个人（认领任务）
//		setAssigneeTask("67502");
		
		//设置流程变量 当前任务
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("message", "no重要");
//		paramMap.put("days", 88);
//		setVariables("42502",paramMap);
		
		//设置流程变量，完成任务
//		Map<String, Object> variables = new HashMap<String, Object>();
//		variables.put("reason", "去泡妞啦");
//		variables.put("days", 6);
//		variables.put("message", "no重要");
//		setVariablesComplete("42502",variables);
		//获取流程变量
//		getVariables("17504");
		
		//获取历史流程变量
		getHisVariables("107501");
		//判断流程是否结束
//		getProcessState("27501");
		
//		queryProcessNow();
	}
	/**
	 * 发布流程
	 */
	public static void publishProcess() {
		/**
		 * 发布流程
		 */
		//获取仓库实例					这里使用RepositoryService部署流程定义  			addClasspathResource表示从类路径下加载资源文件，一次只能加载一个文件
		Deployment deployment = processEngine.getRepositoryService().createDeployment()
				.name("一项多人11groups001")//添加部署规则的显示名称
				.category("请假")//流程分类
				.tenantId("")//租户id  teat006.bpmn2d
				.addClasspathResource("teat006.bpmn").addClasspathResource("teat006.png")//添加定义的规则
				.deploy();//完成发布
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	/**
	 * 启动流程实例 设置流程变量
	 * @param variables  设置审批人
	 */

	public static void startInstanceVariables(Map<String, Object> variables) {
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("feizhi001",variables);
		System.out.println("-----------启动流程实例----------");
		System.out.println(instance.getId());
		System.out.println(instance.getTenantId());
	}
	/**
	 * 启动流程实例
	 */
	public static void startInstance() {
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("groups001");
		System.out.println("-----------启动流程实例----------");
		System.out.println(instance.getId());
		System.out.println(instance.getTenantId());
	}
	/**
	 * 获取我的任务
	 * @param assignee 我
	 */
	public static void queryMyTask(String assignee) {
		//个人查询
//		List<org.activiti.engine.task.Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		//组任务查询
		List<org.activiti.engine.task.Task> tasks = processEngine.getTaskService().createTaskQuery().taskCandidateUser(assignee).list();
		for (org.activiti.engine.task.Task task : tasks) 
			System.out.println("我的任务编号="+task.getId());
			
	}
	/**
	 * 设置流程变量 当前任务
	 * @param taskId 任务编号
	 * @param paramMap 流程变量
	 */
	public static void setVariables(String taskId,Map<String, Object> paramMap) {
		TaskService taskService = processEngine.getTaskService();
//		taskService.setVariables(taskId, paramMap);
		taskService.setVariablesLocal(taskId, paramMap);
	}
	/**
	 * 带着流程变量完成任务
	 * @param taskId 任务
	 * @param variables 变量
	 */
	public static void setVariablesComplete(String taskId,Map<String, Object> variables) {
		processEngine.getTaskService().complete(taskId,variables);
	}
	/**
	 * 完成当前任务，任务编号
	 * @param taskId
	 */
	public static void complete(String taskId) {
		processEngine.getTaskService().complete(taskId);
	}
	
	/**
	 * 	获取流程变量
	 * @param taskId 任务id
	 */
	public static void getVariables(String taskId) {
		TaskService taskService = processEngine.getTaskService();
		Map<String, Object> variales = taskService.getVariables(taskId);
	}
	
	/**
	 * 获取历史流程变量
	 * @param instanceId 流程实例编号
	 */
	public static void getHisVariables(String instanceId) {
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
												.createHistoricVariableInstanceQuery()
												.processInstanceId(instanceId).list();
		System.out.println(list);
	}
	
	
	public static void getProcessState(String processInsanceId){
		//如果instance有多个则抛出异常
		ProcessInstance instance = processEngine.getRuntimeService()	
									.createProcessInstanceQuery()
									.processInstanceId(processInsanceId)
									.singleResult();
		if(null == instance) {
			System.out.println("流程已结束");
		}else {
			System.out.println("当前流程在:"+instance.getActivityId());
		}
	}
	/**
	 * 可以分配个人任务从一个人到另一个人（认领任务）
	 * @param taskId
	 */
	public static void setAssigneeTask(String taskId){
		//指定认领的办理者
		String userId = "周芷若";
		processEngine.getTaskService()//
		.setAssignee(taskId, userId);

	}
	/**
	 * 查看已经部署的流程
	 */
	public static void queryProcessNow() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().latestVersion().list();
		for (ProcessDefinition processDefinition : processDefinitions) {
			System.out.println(processDefinition.getKey() + "     " +processDefinition.getName());
			System.out.println(processDefinition.getResourceName() + "     " +processDefinition.getName());
			
		}
	}
	
	
}

























