package com.mian.tacocloud.repository.jdbc;

import com.mian.tacocloud.domain.Taco;

public interface TacoRepository {

    Taco save(Taco taco);

}