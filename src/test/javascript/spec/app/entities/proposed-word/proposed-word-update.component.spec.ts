/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { ProposedWordUpdateComponent } from 'app/entities/proposed-word/proposed-word-update.component';
import { ProposedWordService } from 'app/entities/proposed-word/proposed-word.service';
import { ProposedWord } from 'app/shared/model/proposed-word.model';

describe('Component Tests', () => {
    describe('ProposedWord Management Update Component', () => {
        let comp: ProposedWordUpdateComponent;
        let fixture: ComponentFixture<ProposedWordUpdateComponent>;
        let service: ProposedWordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ProposedWordUpdateComponent]
            })
                .overrideTemplate(ProposedWordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProposedWordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProposedWordService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ProposedWord(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.proposedWord = entity;
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
                    const entity = new ProposedWord();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.proposedWord = entity;
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
