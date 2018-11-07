package ye.guo.huang;

import java.util.List;

import org.activiti.bpmn.model.Task;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class Test01 {
	
//	@Test
	public void createTable() {
		//创建activity配置对象的实例
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDemo02");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");
		/**
		 * 设置数据库建表策略
		 */
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		
		/**
		 * 获取工作流引擎
		 */
		ProcessEngine processEngine = configuration.buildProcessEngine();
		System.out.println(processEngine);
		
		
		/**
		 * 发布流程
		 */
		//获取仓库实例					这里使用RepositoryService部署流程定义  			addClasspathResource表示从类路径下加载资源文件，一次只能加载一个文件
		Deployment deployment = processEngine.getRepositoryService().createDeployment()
				.name("请假流程名称test001")//添加部署规则的显示名称
				.category("请假")//流程分类
				.tenantId("")//租户id
				.addClasspathResource("test001.bpmn").addClasspathResource("test001.png")//添加定义的规则
				.deploy();//完成发布
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
		
		
		/**
		 * 启动流程实例
		 */
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess");
		System.out.println(instance.getId());
		System.out.println(instance.getTenantId());
		
	}
	
	//@Test
	public void startInstance() {
		//创建-获取工作流引擎对象    ////////////////创建时会自动加载calsspath下的activiti-cfg.xml
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);

		/**
		 * 启动流程实例  7501
		 */
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("testProcess01");
		System.out.println(instance.getId());
		System.out.println(instance.getTenantId());
	}
	
//	@Test
	public void queryMyTask() {
		//创建activity配置对象的实例
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDemo02");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");
		/**
		 * 设置数据库建表策略
		 */
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		
		/**
		 * 获取工作流引擎
		 */
		ProcessEngine processEngine = configuration.buildProcessEngine();
		System.out.println(processEngine);
		
		/**
		 * 获取我的任务
		 */
//		String assignee = "emp";
		String assignee = "manager";
		List<org.activiti.engine.task.Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		for (org.activiti.engine.task.Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getName());
		}
		
		
	}
	
	/**
	 * 7504
	 * 办理任务
	 */
//	@Test
	public void completeTask() {
		//创建-获取工作流引擎对象    ////////////////创建时会自动加载calsspath下的activiti-cfg.xml
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
//		String taskId = "7504";
		String taskId = "10002";
		processEngine.getTaskService().complete(taskId);
		
	}
	
	
	
//	@Test
	public void xmlGetEngine() {
		//创建-获取工作流引擎对象    ////////////////创建时会自动加载calsspath下的activiti-cfg.xml
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
		//仓库对象
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//由仓库服务对象产生一个部署对象，配置对象   用来封装部署操作的相关配置文件
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
	}
	
	/**
	 * 查看流程定义
	 */
//	@Test
	public void queryProcessDefinition() {
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
		
		List<ProcessDefinition> pdList  =  processEngine.getRepositoryService()
											.createProcessDefinitionQuery()
											. orderByProcessDefinitionVersion() .asc ()//查询的结果集
											.list();
		for (ProcessDefinition processDefinition : pdList) {
			System.out.println(processDefinition.getId());
			System.out.println(processDefinition.getName());
			System.out.println(processDefinition.getKey());
			System.out.println(processDefinition.getVersion());
			System.out.println(processDefinition.getDiagramResourceName());
			System.out.println("****************************************");
		}
	}
	/**
	 * 删除流程定义
	 */
//	@Test
	public void delProcessDefinition() {
		String deploymentId = "2501";//部署对象表的id
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
		repositoryService.deleteDeployment(deploymentId,true);
	}
	/**
	 * 查询流程状态
	 */
//	@Test
	public void queryProcessState() {
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println("获取流程processEngine="+processEngine);
		String processInsanceId = "7501";
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
	 * 查询历史任务
	 */
	@Test
	public void queryHistoryTask() {
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println("获取工作流引擎processEngine="+processEngine);
		String taskAssigenn = "emp" ;
		//根据流程实例查该流程实例的所有任务
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
											.createHistoricTaskInstanceQuery()
											.processInstanceId("7501").list();
		//根据流程任务人，查这个人的所有任务
//		List<HistoricTaskInstance> list2 = processEngine.getHistoryService()
//				.createHistoricTaskInstanceQuery().taskAssignee(taskAssigenn).list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println("任务id="+historicTaskInstance.getId());
			System.out.println("流程实例id="+historicTaskInstance.getProcessInstanceId());//7501
			System.out.println("任务的办理人="+historicTaskInstance.getAssignee());
			System.out.println("执行对象id="+historicTaskInstance.getExecutionId());
		}
	}
	
	
	
}
