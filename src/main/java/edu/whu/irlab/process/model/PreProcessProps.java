package edu.whu.irlab.process.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by Roger on 2016/5/17.
 */
@Component
public class PreProcessProps {

    private Properties props = null;

    public Properties getProps() {
        return props;
    }

    @Value("#{preprocess}")
    public void setProps(Properties props) {
        this.props = props;
    }

    public String getProp(String key){
        return  (String)this.props.get(key);
    }
}
