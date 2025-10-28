package com.thy.data;

import java.io.Serializable;

public interface Identifier<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);
}
