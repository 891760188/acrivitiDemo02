package ye.guo.huang;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener {

	@Override
	public void notify(DelegateTask paramDelegateTask) {
		// TODO Auto-generated method stub
		String assignee = "项目经理002";
		paramDelegateTask.setAssignee(assignee);
	}

}
