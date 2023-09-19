package st.cri.app.template.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable


/**
 * Clase dao del que deben extender todos los daos para que permitan
 * specifications y la definicion solo con el nombre del metodo
 * @author jime
 */
@NoRepositoryBean
interface TemplateDao<T, Id : Serializable?> : JpaRepository<T, Id>, JpaSpecificationExecutor<T>
