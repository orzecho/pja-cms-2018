/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { FillingGapsTestItemUpdateComponent } from 'app/entities/filling-gaps-test-item/filling-gaps-test-item-update.component';
import { FillingGapsTestItemService } from 'app/entities/filling-gaps-test-item/filling-gaps-test-item.service';
import { FillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';

describe('Component Tests', () => {
    describe('FillingGapsTestItem Management Update Component', () => {
        let comp: FillingGapsTestItemUpdateComponent;
        let fixture: ComponentFixture<FillingGapsTestItemUpdateComponent>;
        let service: FillingGapsTestItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [FillingGapsTestItemUpdateComponent]
            })
                .overrideTemplate(FillingGapsTestItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FillingGapsTestItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FillingGapsTestItemService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FillingGapsTestItem(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fillingGapsTestItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FillingGapsTestItem();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fillingGapsTestItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
