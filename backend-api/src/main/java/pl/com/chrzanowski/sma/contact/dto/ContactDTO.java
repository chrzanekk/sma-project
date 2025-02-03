package pl.com.chrzanowski.sma.contact.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContactDTO extends AbstractContactDTO {
}