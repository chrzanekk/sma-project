package pl.com.chrzanowski.sma.common.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseCrudController<QD, RD, CD, UD extends HasId<ID>, ID, F> {

    private final BaseCrudService<RD, CD, UD, ID> crudService;
    private final QueryService<QD, F> queryService;

    protected abstract ID extractId(RD rd);

    protected void ensurePathIdMatchesBodyId(ID pathId, UD body) {
        if (body.getId() == null || !body.getId().equals(pathId)) {
            throw new IllegalArgumentException("ID in path and body must match");
        }
    }

    @GetMapping
    public ResponseEntity<List<QD>> list(@ModelAttribute F filter, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return ResponseEntity.ok(queryService.findByFilter(filter));
        }
        var page = queryService.findByFilter(filter, pageable);
        var headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RD> get(@PathVariable ID id) {
        return ResponseEntity.ok(crudService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RD> create(@Valid @RequestBody CD body) {
        RD created = crudService.save(body);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(extractId(created)).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RD> update(@PathVariable ID id, @Valid @RequestBody UD body) {
        ensurePathIdMatchesBodyId(id, body);
        return ResponseEntity.ok(crudService.update(body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        crudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
