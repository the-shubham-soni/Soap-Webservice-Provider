
package allapis.springbootsoap.com;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="employeeInfo" type="{http://com.springbootsoap.allapis}employeeInfo"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "employeeInfo"
})
@XmlRootElement(name = "addEmployeeRequest")
public class AddEmployeeRequest {

    @XmlElement(required = true)
    protected EmployeeInfo employeeInfo;

    /**
     * Gets the value of the employeeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link EmployeeInfo }
     *     
     */
    public EmployeeInfo getEmployeeInfo() {
        return employeeInfo;
    }

    /**
     * Sets the value of the employeeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployeeInfo }
     *     
     */
    public void setEmployeeInfo(EmployeeInfo value) {
        this.employeeInfo = value;
    }

}
