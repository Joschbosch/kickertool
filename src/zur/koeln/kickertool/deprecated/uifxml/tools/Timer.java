package zur.koeln.kickertool.deprecated.uifxml.tools;


public interface Timer {

	void init(int millisecs);
	
    void start();

    void stop();
    
    void pause();
    
    void resume();

    void reset();

}
