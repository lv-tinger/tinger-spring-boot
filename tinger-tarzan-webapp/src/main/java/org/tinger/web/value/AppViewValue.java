package org.tinger.web.value;

import lombok.Builder;
import lombok.Data;
import org.tinger.services.apps.po.AppViewPart;
import org.tinger.services.apps.po.AppViewPartItem;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class AppViewValue implements Serializable {
    public AppViewPart appViewPart;
    public List<AppViewPartItem> appViewPartItems;
}
