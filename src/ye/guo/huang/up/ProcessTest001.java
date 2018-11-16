package ye.guo.huang.up;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class ProcessTest001 {
	private static ProcessEngine processEngine ;
	private static RepositoryService repositoryService ;
	private static RuntimeService runtimeService;
	private static TaskService taskService ;
	
	public static void main(String[] args) {
		//获取工作流引擎
		processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
		getAllService();
//		pushDeploy("HelloWorld");//流程部署
//		startInstance("HelloWorld");//启动流程实例
		findTask("shyroke");
	}
	/**
	 * 获取工作流常用得service
	 */
	private static void getAllService() {
		//获取相关部署的service
		repositoryService = processEngine.getRepositoryService();
		//运行时service
		runtimeService = processEngine.getRuntimeService() ;
		//任务服务
		taskService = processEngine.getTaskService();
	}
	
	/**
	 * 部署流程定义
	 * @param picName 流程图名字
	 */
	private static void pushDeploy(String picName) {
		Deployment deployment = repositoryService.createDeployment()//创建部署
		.addClasspathResource("ye\\guo\\huang\\up\\"+picName+".bpmn")//加载资源文件
		.addClasspathResource("ye\\guo\\huang\\up\\"+picName+".png")//
		.name("流程名称hello world 001")//流程名称
//		.category("请假")//流程分类
//		.tenantId("")//租户id  
		.deploy(); //部署
		
		System.out.println(deployment.getId() + "--" +deployment.getName() + "--" + deployment.getDeploymentTime()+"--"+deployment.getTenantId());
		
	}
	/**
	 * 启动流程实例
	 * @param instanceKey
	 */
	private static void startInstance(String instanceKey) {
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(instanceKey);
		System.out.println("流程实例ID:"+instance.getId()+"       流程定义ID:"+instance.getProcessDefinitionId());
	}
	
	/**
	 * 获取我的任务
	 * @param assignee 审批人
	 */
	private static void findTask(String assignee) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().listPage(0, 100);
		for (Task task : tasks) {
			System.out.println("--------------------------------");
			System.out.println("任务ID:"+task.getId()); 
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务创建时间:"+task.getCreateTime());
            System.out.println("任务委派人:"+task.getAssignee());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
		}
	}
}



















