/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { GapItemUpdateComponent } from 'app/entities/gap-item/gap-item-update.component';
import { GapItemService } from 'app/entities/gap-item/gap-item.service';
import { GapItem } from 'app/shared/model/gap-item.model';

describe('Component Tests', () => {
    describe('GapItem Management Update Component', () => {
        let comp: GapItemUpdateComponent;
        let fixture: ComponentFixture<GapItemUpdateComponent>;
        let service: GapItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [GapItemUpdateComponent]
            })
                .overrideTemplate(GapItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GapItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GapItemService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new GapItem(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gapItem = entity;
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
                    const entity = new GapItem();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gapItem = entity;
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
