/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { WordsTestDeleteDialogComponent } from 'app/entities/words-test/words-test-delete-dialog.component';
import { WordsTestService } from 'app/entities/words-test/words-test.service';

describe('Component Tests', () => {
    describe('WordsTest Management Delete Component', () => {
        let comp: WordsTestDeleteDialogComponent;
        let fixture: ComponentFixture<WordsTestDeleteDialogComponent>;
        let service: WordsTestService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WordsTestDeleteDialogComponent]
            })
                .overrideTemplate(WordsTestDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WordsTestDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordsTestService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
