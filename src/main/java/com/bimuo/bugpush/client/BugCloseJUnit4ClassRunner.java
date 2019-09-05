package com.bimuo.bugpush.client;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class BugCloseJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public BugCloseJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
	@Override
    public void run(RunNotifier notifier)
    {
		//添加自定义Listener
        notifier.addListener(new JUnitListener());
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        
        notifier.fireTestRunStarted(getDescription());
        
        try
        {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        }
        catch(AssumptionViolatedException av)
        {
            testNotifier.addFailedAssumption(av);
        }
		catch (StoppedByUserException sbue)
        {
            throw sbue;
        }
        catch(Throwable e)
        {
            testNotifier.addFailure(e);
        }
    }
}
