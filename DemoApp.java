/************************************************************************************************************************
   Description     : PPT-based System for Cognitive Knowledge Structure Creation
   Source Files    : DemoApp.java, PPTBased_KS_CreationProcess.java, DemoView.java, KSVisualization.java, CommonUtil.java 
   Co-Author       : Kyung Jin Kim / EuGene Jo
                     Dept. of Knowledge Service Engineering
                     Korea Advanced Institute of Science and Technology
   Project Name    : CourseShare - Knowledge Structures
   Team Name       : Knowledge Structures
   Advising Prof.  : Mun Yong Yi 
*************************************************************************************************************************/

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class DemoApp extends SingleFrameApplication {
  @Override
	protected void startup() {
		show(new DemoView(this));
	}
	
	@Override protected void configureWindow(java.awt.Window root) { 
    } 
 
    public static DemoApp getApplication() { 
        return Application.getInstance(DemoApp.class); 
    }    
    
    public static void main(String[] args) { 
       launch(DemoApp.class, args);  
    }    
}
