package com.agravain.filestorage.DAO;

import com.agravain.filestorage.FileDataModel.FileDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDataModel, Integer> {
}
